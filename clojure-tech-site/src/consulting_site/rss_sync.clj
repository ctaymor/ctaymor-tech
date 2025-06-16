(ns consulting-site.rss-sync
  (:require [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.string :as str]
            [clojure.data.zip.xml :as zip-xml]))
;;    (:import (org.jsoup Jsoup)
;;             (org.jsoup.safety Safelist)))
(def rss-cache
  "Deprecated. Stores an in-memory cache of posts for dynamic use"
  (atom {:last-fetch nil :posts []}))

(def cache-lifetime
  "Deprecated. The time when the dynamic post cache should be stale"
  (* 1000 60 60))

(defn cache-expired?
  "Deprecated. Check if the deprecated dynamic cache is stale"
  []
  (let [last-fetch (:last-fetch @rss-cache)]
    (or (nil? last-fetch)
        (> (- (System/currentTimeMillis) last-fetch) cache-lifetime))))

(defn fetch-and-parse-rss
  "Gets data from a remote RSS URL and returns parsed XML.
  Takes a rss-url, which should be the feed url for a valid rss feed.

  This function depends on a network connection.
  "
  [rss-url]
  (try
    (let [response (client/get rss-url)]
      (when (= 200 (:status response))
        (with-open [in (java.io.ByteArrayInputStream. (.getBytes (:body response)))]
          (zip/xml-zip (xml/parse in)))))
    (catch Exception e
      (println "Error fetching RSS:" (.getMessage e))
      nil)))

(defn sanitize-html
  "BROKEN: Sanitizes HTML content to prevent xss attacks, acknowledging whether the source is trusted.
  Takes two args: `content`, which should be the string to be sanitized and `source-url`, which is the url this string originated from.

  NOTE: This function may be broken?? I'm not sure it actually sanitizes anything right now.
  "
  [content source-url]
  (if (and content (not (empty? content)))
    (let [trusted-sources #{"https://write.as/ctaymor" "write.as/ctaymor"}
          is-trusted? (some #(str/includes? (or source-url "") %) trusted-sources)]
      content)
    ""))
(defn create-safe-excerpt
  "Creates a safe, short excerpt from HTML content, handling nil and empty values gracefully.

  Takes `description` which is the full length text to get an excerpt from and `max-length` which is the length to cut the description down to"
  [description max-length]
  (if (and description (not (empty? description)))
    (let [plain-text (-> description
                         (str/replace #"<!\[CDATA\[(.*?)\]\]>" "$1") ;; Extract CDATA content
                         (str/replace #"<[^>]*>" "") ;; Remove all html tags
                         (str/replace #"\n" " "))
          excerpt-length (min (count plain-text) max-length)]
      (if (pos? excerpt-length)
        (str (subs plain-text 0 excerpt-length) "...")
        "No excerpt available"))
    "No excerpt available"))

(defn extract-posts-from-xml
  "Takes zipped XML from an RSS feed and returns a structured keyword map with the context

  Takes `xml-zip` which should be an xml zipper from an RSS feed"
  [xml-zip]
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
(defn fetch-rss-posts
  "Fetches an RSS feed, and returns structured posts from the feed

  Takes a `rss-url` from which to fetch the RSS feed. It should be a valid rss feed endpoint
    TODO: Remove caching logic"
  [rss-url]
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
