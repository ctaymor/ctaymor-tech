; src/consulting_site/core.clj - Main application file
(ns consulting-site.core
  (:require [ring.adapter.jetty :as jetty]
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

;; Server entry point
(defn -main []
  (jetty/run-jetty app {:port 3000 :join? false})
  (println "Server started on port 3000"))
