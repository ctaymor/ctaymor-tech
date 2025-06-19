(ns consulting-site.gcs-cache
  (:require [clojure.data.json :as json])
  (:import (com.google.cloud.storage StorageOptions BlobId BlobInfo)
              (java.nio.charset StandardCharsets)
           (java.security MessageDigest)    ))

(def bucket-name "tech-taymor")
(def cache-prefix "blogpost-cache/v1/")
(def file-suffix "-posts.json")

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
        (println "RSS_UPDATED")
        :updated))))
(defn- getBlob [blobInfo]
  (let [blob (->> (.getBlobId blobInfo)
      (.get (get-storage)))]
    (println (str "BlobInfo is: " (.asBlobInfo blob)))
    blob))

(defn download-latest-posts
  "Download the latest posts from GCS to build/posts.json"
  []
  (try
    (let [storage (get-storage)
          ;; List all blobs with our prefix
          blobs (.list storage bucket-name
                      (into-array com.google.cloud.storage.Storage$BlobListOption
                                  [(com.google.cloud.storage.Storage$BlobListOption/prefix cache-prefix)]))
          ;; Filter to just the posts.json files and sort by creation time
          posts-blobs (->> (.iterateAll blobs)
                          (filter #(.endsWith (.getName %) file-suffix))
                          (sort-by #(.getCreateTime %) #(compare %2 %1))) ;; newest first
          latest-blob-info (first posts-blobs)]

      (if latest-blob-info
        (do
          ;; Create build directory if it doesn't exist
          (.mkdirs (java.io.File. "build"))
          ;; Download the blob content
          (let [blob_id (.getBlobId latest-blob-info)
                content (.readAllBytes storage blob_id (make-array com.google.cloud.storage.Storage$BlobSourceOption 0))
                json-str (String. content StandardCharsets/UTF_8)
                ]
            (spit "build/posts.json" json-str)
            (println "Downloaded to build/posts.json")
            0)
          )
        (do
          (println "No posts files found in GCS")
          1))) ;; no files exit code
    (catch Exception e
      (println "Error downloading from GCS:" (.getMessage e))
      2))) ;; error exit code