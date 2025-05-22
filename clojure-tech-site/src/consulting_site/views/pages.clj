; src/consulting_site/views/pages.clj - Individual page content
(ns consulting-site.views.pages
  (:require
   [clojure.string :as str]
   [hickory.core :as hickory]
   [hickory.select :as s]
   [consulting-site.config :refer [site-info]]))




(defn blog-page [posts]
  [:div.blog-content
   [:h1 "Blog"]
   [:div.blog-intro
    [:p "Thoughts, insights, and expertise on software engineering, architecture, and technology."]]

   [:div.blog-posts
    (for [post posts]
      [:div.blog-post-preview
       [:h2 [:a {:href (str "/blog/" (:id post))} (:title post)]]
       [:div.post-meta
        [:span.post-date (:date post)]
        [:span.post-author " By " (:author post)]]
       [:div.post-excerpt (:excerpt post)]
       [:a.read-more {:href (str "/blog/" (:id post))} "Read More"]])]])

;; Convert HTML string to Hiccup data structure for sanitizing
(defn html-to-hiccup [html-str]
  (try
    (let [parsed (-> html-str
                     hickory/parse)
          hiccup-version (hickory/as-hiccup parsed)
          ;; Filter out dangerous tags (you'll need to adapt this for hiccup format)
          safe-hiccup (filter #(not (contains?
                                     #{"script" "iframe" "object" "embed"}
                                     (when (vector? %) (name (first %)))))
                              hiccup-version)]
      (println "type of safe-hiccup:" (type safe-hiccup))
      safe-hiccup)
    (catch Exception e
      (println "Error converting HTML to Hiccup:" (.getMessage e))
      [:div [:p "Error rendering content"]])))

;; Simplified blog-post-page that uses Hiccup for rendering
(defn blog-post-page [post]
  [:div.blog-post
   [:h1 (:title post)]
   [:div.post-meta
    [:span.post-date (:date post)]
    [:span.post-author "By " (:author post)]]

   ;; Main content section using Hiccup for rendering
   [:div.post-content
    (html-to-hiccup (:content post))]

   [:div.post-navigation
    [:a.prev-post {:href "/blog"} "← Back to Blog"]]])

(defn not-found-page []
  [:div.not-found
   [:h1 "404 - Page Not Found"]
   [:p "Sorry, the page you're looking for doesn't exist."]
   [:a.button {:href "/"} "Return to Home"]])