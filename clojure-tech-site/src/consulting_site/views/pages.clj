; src/consulting_site/views/pages.clj - Individual page content
(ns consulting-site.views.pages
  (:require
   [clojure.string :as str]
   [hickory.core :as hickory]
   [hickory.select :as s]))

(defn home-page []
  [:div.home-content
   [:section.hero
    [:h1 "Software Engineering Excellence"]
    [:p.tagline "Building robust, scalable solutions for modern businesses"]
    [:div.cta-buttons
     [:a.button.primary {:href "/contact"} "Get in Touch"]
     [:a.button.secondary {:href "/services"} "Our Services"]]]

   [:section.features
    [:h2 "Why Choose Us"]
    [:div.feature-grid
     [:div.feature
      [:h3 "Expert Consulting"]
      [:p "Leverage years of experience across various technology domains."]]
     [:div.feature
      [:h3 "Custom Solutions"]
      [:p "Tailored development to match your unique business requirements."]]
     [:div.feature
      [:h3 "Modern Technologies"]
      [:p "Utilizing cutting-edge tools and practices for optimal results."]]]]

   [:section.recent-posts
    [:h2 "Recent Blog Posts"]
    [:div#recent-posts-content
     [:p "Loading recent posts..."]
     [:script "document.addEventListener('DOMContentLoaded', function() {
               fetch('/blog').then(response => {
                 if(response.ok) window.location.href = '/blog';
               });
             });"]]]])

(defn about-page []
  [:div.about-content
   [:h1 "About Us"]
   [:section.about-intro
    [:div.about-text
     [:h2 "Who We Are"]
     [:p "Founded in 2020, Your Name Consulting provides expert software engineering services to businesses across various industries. With a focus on quality, performance, and maintainability, we help our clients build software that truly delivers value."]
     [:p "Our team brings decades of combined experience in software architecture, development, and technical leadership to every project we undertake."]]
    [:div.about-image
     [:img {:src "/img/team.jpg" :alt "Our Team"}]]]

   [:section.expertise
    [:h2 "Our Expertise"]
    [:ul.expertise-list
     [:li "Back-end systems development"]
     [:li "Distributed systems architecture"]
     [:li "Database design and optimization"]
     [:li "API development and integration"]
     [:li "DevOps and infrastructure automation"]
     [:li "Functional programming"]
     [:li "Cloud-native applications"]]]])

(defn services-page []
  [:div.services-content
   [:h1 "Our Services"]

   [:section.service
    [:h2 "Software Architecture Consulting"]
    [:p "We help you design robust, scalable, and maintainable software architectures tailored to your business needs."]
    [:ul
     [:li "System design and architecture review"]
     [:li "Technical debt assessment"]
     [:li "Scalability planning"]
     [:li "Technology stack recommendations"]]]

   [:section.service
    [:h2 "Custom Software Development"]
    [:p "End-to-end development of custom software solutions built with modern best practices."]
    [:ul
     [:li "Web applications and services"]
     [:li "API development and integration"]
     [:li "Data processing pipelines"]
     [:li "Cloud-native applications"]]]

   [:section.service
    [:h2 "Technical Leadership"]
    [:p "Experienced technical leadership to guide your team and projects toward success."]
    [:ul
     [:li "Team mentoring and training"]
     [:li "Code review and quality processes"]
     [:li "Development workflow optimization"]
     [:li "Technical strategy planning"]]]])

(defn contact-page []
  [:div.contact-content
   [:h1 "Contact Us"]
   [:div.contact-container
    [:div.contact-info
     [:h2 "Get In Touch"]
     [:p "We'd love to hear about your project. Reach out to discuss how we can help with your software engineering needs."]
     [:ul.contact-details
      [:li [:i.icon.email] "your.email@example.com"]
      [:li [:i.icon.phone] "(123) 456-7890"]
      [:li [:i.icon.location] "San Francisco, CA"]]]

    [:div.contact-form
     [:h2 "Send a Message"]
     [:form {:action "/submit-contact" :method "post"}
      [:div.form-group
       [:label {:for "name"} "Name"]
       [:input#name {:type "text" :name "name" :required true}]]
      [:div.form-group
       [:label {:for "email"} "Email"]
       [:input#email {:type "email" :name "email" :required true}]]
      [:div.form-group
       [:label {:for "subject"} "Subject"]
       [:input#subject {:type "text" :name "subject"}]]
      [:div.form-group
       [:label {:for "message"} "Message"]
       [:textarea#message {:name "message" :rows "5" :required true}]]
      [:button.button.primary {:type "submit"} "Send Message"]]]]])

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