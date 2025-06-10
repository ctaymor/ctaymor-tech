; src/consulting_site/views/pages.clj - Individual page content
(ns consulting-site.views.pages
  (:require
   [clojure.string :as str]
   [hickory.core :as hickory]
   [hickory.select :as s]
   [consulting-site.config :refer [site-info]]))

;; Simplified blog-post-page that uses Hiccup for rendering

(defn not-found-page []
  [:div.not-found
   [:h1 "404 - Page Not Found"]
   [:p "Sorry, the page you're looking for doesn't exist."]
   [:a.button {:href "/"} "Return to Home"]])