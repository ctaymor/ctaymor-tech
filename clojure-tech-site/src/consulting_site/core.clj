; src/consulting_site/core.clj - Main application file
(ns consulting-site.core
  (:require [ring.adapter.jetty :as jetty]
            [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.resource :refer [wrap-resource]]
            [hiccup.page :refer [html5 include-css include-js]]
            [consulting-site.views.layout :as layout]
            [consulting-site.views.pages :as pages]
            [consulting-site.views.pages
             [home :as home]
             [services :as services]
             [contact :as contact]
             [about :as about]
             [blog :as blog-views]]
            [consulting-site.blog :as blog]
            ))

;; Add this after your requires, before defroutes
(println "Available in blog-views:" (keys (ns-publics 'consulting-site.views.pages.blog)))

;; Define our routes
(defroutes app-routes
  (GET "/" [] (layout/render-page (home/page)))
  (GET "/about" [] (layout/render-page (about/page)))
  (GET "/services" [] (layout/render-page (services/page)))
  (GET "/contact" [] (layout/render-page (contact/page)))
  (GET "/blog" [] (layout/render-page (blog-views/page (blog/get-recent-posts))))
  (GET "/blog/:id" [id] (layout/render-page (blog-views/post (blog/get-post-by-id id))))
  (route/resources "/")
  (route/not-found (layout/render-page (pages/not-found-page))))

;; Configure our app with middleware
(def app
  (-> app-routes
      (wrap-resource "public")
      (wrap-defaults site-defaults)
      (wrap-content-type)))

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
    (let [posts (blog/load-local-posts)]
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
  (if (= (first args) "build")
    (generate-static-site)
    (do
      (jetty/run-jetty app {:port 3000 :join? false})
      (println "Server started on port 3000"))))

