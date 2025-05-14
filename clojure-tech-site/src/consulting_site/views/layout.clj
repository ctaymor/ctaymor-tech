; src/consulting_site/views/layout.clj - Page layout/template
(ns consulting-site.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [consulting-site.config :refer [site-info]]))

(defn render-page [content]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title (str)]
    (include-css "/css/style.css")
    (include-js "/js/script.js")]
   [:body
    [:header
     [:div.logo (:company-name site-info)]
     [:nav
      [:ul
       [:li [:a {:href "/"} "Home"]]
       [:li [:a {:href "/about"} "About"]]
       [:li [:a {:href "/services"} "Services"]]
       [:li [:a {:href "/blog"} "Blog"]]
       [:li [:a {:href "/contact"} "Contact"]]]]]

    [:main content]

    [:footer
     [:div.footer-content
      [:div.footer-section
       [:h3 (:company-name site-info)]
       [:p (:tagline site-info)]]
      [:div.footer-section
       [:h3 "Contact"]
       [:p (str "Email: ", (:email site-info))]]
      [:div.footer-section
       [:h3 "Connect"]
       [:div.social-links
        [:a {:href (get-in site-info [:social-links :linkedin])} "LinkedIn"]]]]
     [:div.copyright
      [:p (str "© " (:year site-info) " " (:company-name site-info) ". All rights reserved.")]]]]))