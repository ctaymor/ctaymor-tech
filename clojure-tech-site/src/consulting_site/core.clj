; src/consulting_site/core.clj - Main application file
(ns consulting-site.core
  (:require [clojure.java.io :as io]
            [hiccup.page :refer [html5 include-css include-js]]
            [consulting-site.views.layout :as layout]
            [consulting-site.views.pages :as pages]
            [consulting-site.views.pages
             [home :as home]
             [services :as services]
             [contact :as contact]
             [about :as about]
             [blog :as blog-views]]
            [consulting-site.blog :as blog])
  (:gen-class))

(defn copy-resources []
  "Copy static resources from resources/public to dist"
  (let [source-dir "resources/public/"
        dest-dir "dist/"]
    (when (.exists (java.io.File. source-dir))
      ;; Copy all files from resources/public to dist
      (doseq [file (file-seq (io/file source-dir))]
        (when (.isFile file)
          (let [relative-path (.substring (.getPath file) (count source-dir))
                dest-file (io/file dest-dir relative-path)]
            (io/make-parents dest-file)
            (io/copy file dest-file)))))))

(defn generate-static-site []
  (let [output-dir "dist/"]
    (.mkdirs (java.io.File. output-dir))

    ;; Create directories for each route
    (.mkdirs (java.io.File. (str output-dir "about/")))
    (.mkdirs (java.io.File. (str output-dir "services/")))
    (.mkdirs (java.io.File. (str output-dir "contact/")))
    (.mkdirs (java.io.File. (str output-dir "blog/")))

    ;; Generate as index.html in each directory
    (spit (str output-dir "index.html") (layout/render-page (home/page)))
    (spit (str output-dir "about/index.html") (layout/render-page (about/page)))
    (spit (str output-dir "services/index.html") (layout/render-page (services/page)))
    (spit (str output-dir "contact/index.html") (layout/render-page (contact/page)))

    ;; Blog
    (let [posts (blog/load-posts)]
      (spit (str output-dir "blog/index.html") (layout/render-page (blog-views/page posts)))

      ;; Individual posts
      (doseq [post posts]
        (let [post-dir (str output-dir "blog/" (:id post) "/")]
          (.mkdirs (java.io.File. post-dir))
          (spit (str post-dir "index.html") (layout/render-page (blog-views/post post))))))

    (copy-resources)
    (println "Static site generated in" output-dir)))

;; Entry point
(defn -main [& args]
  (case (first args)
    "build" (generate-static-site)
    "rss-sync" (println "RSS sync placeholder - will implement next")
    (println "Commands: 'build' or 'sync-rss'")))