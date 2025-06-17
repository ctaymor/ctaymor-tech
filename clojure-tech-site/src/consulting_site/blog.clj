; src/consulting_site/blog.clj - Blog functionality with RSS integration
(ns consulting-site.blog
  (:require
   [clojure.data.json :as json]
   [clojure.string :as str]
   [clojure.java.io :as io]
   [consulting-site.rss-sync :as rss-sync]))

(defn load-built-posts
  "Load blog posts from the build/posts.json file.

  These are generated during the build phase"
  []
    (try
      (let [posts-file (io/file "build/posts.json")]
        (if (.exists posts-file)
          (json/read-str (slurp posts-file) :key-fn keyword)
          []))
      (catch Exception e
        []))
  )
(defn load-local-posts
  "Load blog posts from the resources/posts.json file"
  []
  (try
    (let [posts-file (io/resource "posts.json")]
      (if posts-file ; Note that resources must be checked differently than files
        (json/read-str (slurp posts-file) :key-fn keyword)
        []))
    (catch Exception e
      [])))
(defn load-posts
  []
  (let [built-posts (load-built-posts)]
    (if (seq built-posts)
      built-posts
      (load-local-posts))))

(defn get-recent-posts
  "Deprecated. Fetch posts from my write.as feed, and if that fails, fetch from local posts."
  []
  (let [rss-url "https://write.as/ctaymor/feed/" ;; Replace with your actual Write.as RSS feed URL
        rss-posts (rss-sync/sync-rss-to-json rss-url)]
    (if (seq rss-posts)
      rss-posts
      (load-local-posts))))

(defn get-post-by-id
  "Get a single post by id
  Takes an `id` which should be the id of a post."
  [id]
  (let [posts (load-posts)]
    (first (filter #(= (:id %) id) posts))))

