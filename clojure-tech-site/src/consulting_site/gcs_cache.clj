(ns consulting-site.gcs-cache
  (:require [clojure.data.json :as json])
  (:import (com.google.cloud.storage StorageOptions BlobId BlobInfo)
              (java.nio.charset StandardCharsets)))

(def bucket-name "tech-taymor")
(def cache-prefix "blogpost-cache/v1/")

(defn- get-storage []
  (-> (StorageOptions/getDefaultInstance)
      (.getService)))

(defn upload-posts [posts-json]
  (let [storage (get-storage)
        blob-id (BlobId/of bucket-name (str cache-prefix "posts.json"))
        blob-info (-> (BlobInfo/newBuilder blob-id)
                      (.setContentType "application/json")
                      (.build))
        json-bytes (.getBytes (json/write-str posts-json) StandardCharsets/UTF_8)]
   (.create storage blob-info json-bytes (make-array com.google.cloud.storage.Storage$BlobTargetOption 0))
    (println "Uploaded posts.json to GCS")))
