; src/consulting_site/views/layout.clj - Page layout/template
(ns consulting-site.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]))

(defn render-page [content]
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     [:title "Your Name - Software Engineering Consultant"]
     (include-css "/css/style.css")
     (include-js "/js/script.js")]
    [:body
     [:header
      [:div.logo "Your Name"]
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
        [:h3 "Your Name Consulting"]
        [:p "Professional Software Engineering Solutions"]]
       [:div.footer-section
        [:h3 "Contact"]
        [:p "Email: your.email@example.com"]
        [:p "Phone: (123) 456-7890"]]
       [:div.footer-section
        [:h3 "Connect"]
        [:div.social-links
         [:a {:href "#"} "GitHub"]
         [:a {:href "#"} "LinkedIn"]
         [:a {:href "#"} "Twitter"]]]]
      [:div.copyright
       [:p "© 2025 Your Name Consulting. All rights reserved."]]]
       ]
    )
)