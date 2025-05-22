; src/consulting_site/views/pages.clj - Individual page content
(ns consulting-site.views.pages
  (:require
   [clojure.string :as str]
   [hickory.core :as hickory]
   [hickory.select :as s]
   [consulting-site.config :refer [site-info]]))



(defn about-page []
  [:div.about-content
   [:h1 "About Us"]
   [:section.about-intro
    [:div.about-text
     [:h2 "Who We Are"]
     [:p (str "Founded in 2020, " (:company-name site-info) "provides expert software engineering services to businesses across various industries. With a focus on quality, performance, and maintainability, we help our clients build software that truly delivers value.")]

     [:h1 "About Caroline Taymor"]
     [:p "As a Bay Area native and Berkeley resident, I founded Caroline Taymor Consulting in 2022 to create more balance in my life while continuing to do meaningful technical work. I help teams tackle complex technical challenges through a blend of deep systems expertise and structured collaboration approaches."]
     [:p "My unconventional career path—from managing a community kitchen to earning a BS in Mathematics as a non-traditional student, to becoming both a Staff Engineer and Engineering Manager at Pivotal—gives me a unique perspective on both technical systems and human factors. At Pivotal, I specialized in transforming struggling teams: rescuing teams from alert fatigue, guiding teams through intimidating technical challenges they'd been avoiding, and automating critical processes (like reducing a 12-hour manual security workflow to 2 hours of mostly automated work)."]
     [:p "I excel where others get stuck—in haunted codebases, complex debugging missions, and when teams are overwhelmed or misaligned. I bring proven methodologies like whiteboard pair programming, scientific debugging approaches, and structured decision-making tools that help teams move from paralyzed to productive."]
     [:p "When I'm not solving technical problems, I'm tending my front yard urban farm (where I work out many technical problems while pruning tomatoes), studying Talmud with my chevruta, or using fiber crafts as meditation. I'm most proud of building the legal hotline for Repro Equity Now—a service that helps Massachusetts abortion providers navigate shield law protections when serving out-of-state patients."]]
    [:div.about-image
     [:img {:src "/images/carolineTaymorHeadshot2025.jpg" :alt "Headshot of Caroline Taymor, a white curly haired person in a black shirt with red overshirt in front of dogwoods"}]]]

   [:section.expertise
    [:h2 "Technical Expertise"]
    [:ul.expertise-list
     [:li "Back-end systems development"]
     [:li "Distributed systems architecture"]
     [:li "API development and integration"]
     [:li "DevOps and infrastructure automation"]
     [:li "Cloud-native applications"]]]])

(defn services-page []
  [:div.services-content
   [:h1 "Services"]

   [:section.service
    [:h2 "Technical Leadership"]
    [:p "Experienced fractional technical leadership to guide your team through complex challenges."]
    [:ul
     [:li "Breaking decision paralysis on complex technical decisions"]
     [:li "Facilitating cross-functional collaboration"]
     [:li "Aligning stakeholders and engineering teams"]
     [:li "Building sustainable engineering practices"]]]

   [:section.service
    [:h2 "System Debugging & Improvement"]
    [:p "Expert debugging and system improvement for complex, business-critical codebases."]
    [:ul
     [:li "Systematic debugging of \"haunted\" legacy systems"]
     [:li "Implementing automation for error-prone processes"]
     [:li "Reducing operational burden and on-call pain"]
     [:li "Improving deployment reliability and confidence"]]]

   [:section.service
    [:h2 "Team Capability Building"]
    [:p "Building lasting engineering capability within your team through hands-on collaboration."]
    [:ul
     [:li "Deliberate pairing and knowledge transfer"]
     [:li "Teaching systematic debugging approaches"]
     [:li "Modeling iterative software engineering practices"]
     [:li "Establishing sustainable on-call and incident response processes"]]]])

(defn contact-page []
  [:div.contact-content
   [:h1 "Contact Us"]
   [:div.contact-container
    [:div.contact-info
     [:h2 "Get In Touch"]
     [:p "We'd love to hear about your project. Reach out to discuss how we can help with your software engineering needs."]
     [:contact-details
      [:span "📧 "] [:a {:href (str "mailto:" (:email site-info) "?subject=Consulting Inquiry")}
                                                   (:email site-info)]]]

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