(ns consulting-site.gcs-cache
  (:require [clojure.data.json :as json])
  (:import (com.google.cloud.storage StorageOptions BlobId BlobInfo)
              (java.nio.charset StandardCharsets)
           (java.security MessageDigest)    ))

(def bucket-name "tech-taymor")
(def cache-prefix "blogpost-cache/v1/")
(def file-suffix "-posts.json")

(defn- create-rebuild-marker []
  (spit ".rebuild_needed" "rebuild needed"))

(defn- content-hash
  "Get a SHA256 hash of the input"
  [posts-json]
  (let [json-str (json/write-str posts-json)
        md (MessageDigest/getInstance "SHA-256")
        hash-bytes (.digest md (.getBytes json-str StandardCharsets/UTF_8))]
    (->> hash-bytes
         (map #(format "%02x" %))
         (apply str))))

(defn- hash-exists?
  "Check if there is a file with <CONTENT-HASH>-posts.json in the storage bucket.

  This would indicate that the RSS feed is up to date and we don't need to reload it"
  [storage content-hash]
  (let [blob-id (BlobId/of bucket-name (str cache-prefix content-hash file-suffix))]
    (some? (.get storage blob-id))))

(defn- get-storage
  "Get the GCP Storage service object"
  []
  (-> (StorageOptions/getDefaultInstance)
      (.getService)))

(defn upload-posts
  "Upload the posts to gcp.

  Checks the hash of the contents, and if
  there is no file in the bucket with that hash as a prefix, uploads the contents
  to <HASH>-posts.json, and creates a .rebuild-needed file locally.

  If the file with that hash exists, it exits and prints no changes"
  [posts-json]
  (let [storage (get-storage)
        hash (content-hash posts-json)]
    (if (hash-exists? storage hash)
      (do
        (println "RSS_NO_CHANGES")
        :no-changes)
      (let [blob-id (BlobId/of bucket-name (str cache-prefix hash file-suffix))
            blob-info (-> (BlobInfo/newBuilder blob-id)
                          (.setContentType "application/json")
                          (.build))
            json-str (json/write-str posts-json)
            json-bytes (.getBytes json-str StandardCharsets/UTF_8)]
        (.create storage blob-info json-bytes (make-array com.google.cloud.storage.Storage$BlobTargetOption 0))
        (create-rebuild-marker)  ; Creates local .rebuild_needed file
        (println "RSS_UPDATED")
        :updated))))