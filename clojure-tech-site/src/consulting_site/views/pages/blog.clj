(ns consulting-site.views.pages.blog
  (:require
    [consulting-site.views.helpers :as helpers]
  ))

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

(defn blog-post-page [post]
  [:div.blog-post
   [:h1 (:title post)]
   [:div.post-meta
    [:span.post-date (:date post)]
    [:span.post-author "By " (:author post)]]

   ;; Main content section using Hiccup for rendering
   [:div.post-content
    (helpers/html-to-hiccup (:content post))]

   [:div.post-navigation
    [:a.prev-post {:href "/blog"} "← Back to Blog"]]])


(def page blog-page)
(def post blog-post-page)