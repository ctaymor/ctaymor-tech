(ns consulting-site.rss-sync
  (:require [clj-http.client :as client]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.string :as str]
            [clojure.data.zip.xml :as zip-xml]
            [clojure.data.json :as json]
            [clojure.java.io :as io]))

(def allowed-sources
  #{"https://write.as/ctaymor" "write.as/ctaymor"})

(defn trusted-source? [url]
  (contains? allowed-sources url))

(defn fetch-rss-as-xml
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
  "Sanitize html in a given string. Strings from trusted URLs should have html tags allowed"
  [html]
  (-> html
      ; Remove dangerous tags entirely
      (str/replace #"<(?:script|style|iframe)[^>]*>.*?</(?:script|style|iframe)>" "")
      ; Keep only safe tags, strip everything else
      (str/replace #"<(?!/?(p|strong|em|a|ul|ol|li|br)\b)[^>]*>" "")))

(defn decode-html-entities [text]
  (-> text
      (clojure.string/replace #"&amp;" "&")
      (clojure.string/replace #"&lt;" "<")
      (clojure.string/replace #"&gt;" ">")
      (clojure.string/replace #"&quot;" "\"")))

(defn strip-html
  "Remove ALL html tags from the text and creates a plain string.

  This removes known safe tags as well. This is useful for excerpting, where character count matters and html could mess with that in weird ways"
  [text]
  (-> text
      (decode-html-entities)
      (str/replace #"<[^>]*>" "")))

(defn sanitize-html
  "Remove all html tags other than a handful of known safe tags for links and formatting."
  [text]
  (-> text
      (decode-html-entities)
      (str/replace #"<(?!\/?(p|strong|em|a|ul|ol|li|br)\b)[^>]*>" "")
      ))

(defn create-safe-excerpt
  "Creates a safe, short excerpt from HTML content, handling nil and empty values gracefully.

  Takes `description` which is the full length text to get an excerpt from and `max-length` which is the length to cut the description down to"
  [description max-length]
  (if (and description (not (empty? description)))
    (let [plain-text (-> description
                         (str/replace #"<!\[CDATA\[(.*?)\]\]>" "$1") ;; Extract CDATA content
                         (strip-html)
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
         :title (or (sanitize-html title) "Untitled")
         :date (or pubDate "Unknown Date")
         :link link
         :excerpt (create-safe-excerpt (or description content) 500)
         :content (sanitize-html content)
         :author "Caroline Taymor"}))))

(defn write-json-to-file [posts]
  (let [filename "build/posts.json"] ; or make this configurable
    (io/make-parents filename)
    (with-open [writer (io/writer filename)]
      (json/write posts writer))
    posts))

(defn sync-rss-to-json [url]
  (trusted-source? url)
  (-> (fetch-rss-as-xml url)
      (extract-posts-from-xml)
      (write-json-to-file)))












