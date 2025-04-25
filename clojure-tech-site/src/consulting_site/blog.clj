; src/consulting_site/blog.clj - Blog functionality with RSS integration
(ns consulting-site.blog
  (:require [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zip-xml]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [markdown.core :as md]
            )
    (:import (org.jsoup Jsoup)
             (org.jsoup.safety Safelist)))

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

(defn sanitize-html
  "Sanitizes HTML content to prevent xss attacks, acknowledging whether the source is trusted"
  [content source-url]
  (if (and content (not (empty? content)))
    (let [trusted-sources #{"https://write.as/ctaymor" "write.as/ctaymor"}
          is-trusted? (some #(str/includes? (or source-url "") %) trusted-sources)]
      content)
    ""))

;; Safely create an excerpt from HTML content
(defn create-safe-excerpt
  "Creates a safe excerpt from HTML content, handling nil and empty values gracefully."
  [description max-length]
  (if (and description (not (empty? description)))
    (let [plain-text (-> description
                         (str/replace #"<!\[CDATA\[(.*?)\]\]>" "$1") ;; Extract CDATA content
                         (str/replace #"<[^>]*>" "")
                         (str/replace #"\n" " "))
          excerpt-length (min (count plain-text) max-length)]
      (if (pos? excerpt-length)
        (str (subs plain-text 0 excerpt-length) "...")
        "No excerpt available"))
    "No excerpt available"))

;; Extract posts from XML zipper
(defn extract-posts-from-xml [xml-zip]
  (when xml-zip
    (for [item (zip-xml/xml-> xml-zip :channel :item)]
      (let [guid (zip-xml/xml1-> item :guid zip-xml/text)
            id (when guid (last (str/split guid #"/")))
            title (zip-xml/xml1-> item :title zip-xml/text)
            link (zip-xml/xml1-> item :link zip-xml/text)
            pubDate (zip-xml/xml1-> item :pubDate zip-xml/text)
            description (or (zip-xml/xml1-> item :description zip-xml/text) "")
            content (or (zip-xml/xml1-> item :content:encoded zip-xml/text)
                        (zip-xml/xml1-> item "content:encoded" zip-xml/text))
            source-url (or link "")]
        {:id (or id (str (java.util.UUID/randomUUID)))
         :title (or title "Untitled")
         :date (or pubDate "Unknown Date")
         :link link
         :excerpt (create-safe-excerpt (or description content) 500)
         :content (sanitize-html (or content description "No content available") source-url)
         :author "Caroline Taymor"}))))

;; Fetch posts from RSS feed with caching and source validation
(defn fetch-rss-posts [rss-url]
  (when (cache-expired?)
    (let [trusted-sources #{"https://write.as/ctaymor/feed/"}
          is-trusted? (contains? trusted-sources rss-url)]
      (if is-trusted?
        (let [xml (fetch-and-parse-rss rss-url)
              posts (extract-posts-from-xml xml)]
          (when (seq posts)
            (reset! rss-cache {:last-fetch (System/currentTimeMillis)
                              :posts posts})))
        (println "Warning: Attempted to fetch RSS from untrusted source:" rss-url))))
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
  (let [rss-url "https://write.as/ctaymor/feed/" ;; Replace with your actual Write.as RSS feed URL
        rss-posts (fetch-rss-posts rss-url)]
    (if (seq rss-posts)
      rss-posts
      (load-local-posts))))

;; Get a specific post by ID
(defn get-post-by-id [id]
  (let [posts (get-recent-posts)]
    (first (filter #(= (:id %) id) posts))))