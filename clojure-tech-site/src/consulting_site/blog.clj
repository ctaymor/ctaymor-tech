; src/consulting_site/blog.clj - Blog functionality with RSS integration
(ns consulting-site.blog
  (:require [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zip-xml]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [markdown.core :as md]))

;; Cache for RSS feed to avoid frequent fetching
(def rss-cache (atom {:last-fetch nil :posts []}))

;; Time to consider cache stale (1 hour in milliseconds)
(def cache-lifetime (* 1000 60 60))

(defn cache-expired? []
  (let [last-fetch (:last-fetch @rss-cache)]
    (or (nil? last-fetch)
        (> (- (System/currentTimeMillis) last-fetch) cache-lifetime))))

;; Parse RSS feed from Write.as
(defn fetch-and-parse-rss [rss-url]
  (try
    (let [response (client/get rss-url)]
      (when (= 200 (:status response))
        (with-open [in (java.io.ByteArrayInputStream. (.getBytes (:body response)))]
          (zip/xml-zip (xml/parse in)))))
    (catch Exception e
      (println "Error fetching RSS:" (.getMessage e))
      nil)))

;; Extract posts from XML zipper
(defn extract-posts-from-xml [xml-zip]
  (when xml-zip
    (for [item (zip-xml/xml-> xml-zip :channel :item)]
      (let [guid (zip-xml/xml1-> item :guid zip-xml/text)
            id (last (str/split (or guid "") #"/"))
            title (zip-xml/xml1-> item :title zip-xml/text)
            link (zip-xml/xml1-> item :link zip-xml/text)
            pubDate (zip-xml/xml1-> item :pubDate zip-xml/text)
            description (zip-xml/xml1-> item :description zip-xml/text)
            content (zip-xml/xml1-> item :encoded zip-xml/text)]
        {:id id
         :title title
         :date pubDate
         :link link
         :excerpt (-> description
                      (str/replace #"<[^>]*>" "")
                      (str/replace #"\n" " ")
                      (subs 0 (min (count description) 200))
                      (str "..."))
         :content (or content description)
         :author "Your Name"}))))

;; Fetch posts from RSS feed with caching
(defn fetch-rss-posts [rss-url]
  (when (cache-expired?)
    (let [xml (fetch-and-parse-rss rss-url)
          posts (extract-posts-from-xml xml)]
      (when (seq posts)
        (reset! rss-cache {:last-fetch (System/currentTimeMillis)
                          :posts posts}))))
  (:posts @rss-cache))

;; Fallback to locally stored posts if RSS fails
(defn load-local-posts []
  (try
    (let [posts-file (io/resource "posts.json")]
      (if posts-file
        (json/read-str (slurp posts-file) :key-fn keyword)
        []))
    (catch Exception e
      (println "Error loading local posts:" (.getMessage e))
      [])))

;; Get recent posts, prioritizing RSS feed
(defn get-recent-posts []
  (let [rss-url "https://your-blog.write.as/feed/" ;; Replace with your actual Write.as RSS feed URL
        rss-posts (fetch-rss-posts rss-url)]
    (if (seq rss-posts)
      rss-posts
      (load-local-posts))))

;; Get a specific post by ID
(defn get-post-by-id [id]
  (let [posts (get-recent-posts)]
    (first (filter #(= (:id %) id) posts))))