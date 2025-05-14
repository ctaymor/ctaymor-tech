; src/consulting_site/views/pages.clj - Individual page content
(ns consulting-site.views.pages
  (:require
   [clojure.string :as str]
   [hickory.core :as hickory]
   [hickory.select :as s]
   [consulting-site.config :refer [site-info]]))

(defn home-page []
  [:div.home-content
   [:section.hero
    [:h1 "Fractional Software Engineering Leadership"]
    [:p.tagline "Unsticking Teams Through Engineering Rigor and Collaboration"]
    [:div.cta-buttons
     [:a.button.primary {:href "/contact"} "Get in Touch"]
     [:a.button.secondary {:href "/services"} "Learn More"]]]

   [:section.when-to-bring-me-in
    [:h2 "When to Bring Me In"]
    [:div.feature-grid
     [:div.feature
      [:p "Complex technical challenges have your team stuck or divided"]]
     [:div.feature
      [:p "Drowning in operational burdens with no clear path to improvement"]]
     [:div.feature
      [:p "Critical debugging missions keep expanding with no resolution in sight"]]
     [:div.feature
      [:p "Deployment process is causing pain, delays, or reliability issues"]]]]

   [:section.approach
    [:h2 "My Approach"]

    [:div.approach-section
     [:h3 "Technical Depth That Cuts Through Complexity"]
     [:ul.approach-list
      [:li "Systematic debugging of \"haunted\" codebases—large, long-running, highly coupled, business-critical systems where institutional knowledge has been lost"]
      [:li "Automation that transforms painful, error-prone processes into repeatable, junior-friendly workflows"]
      [:li "Clear-eyed application of engineering practices that match your team's real needs, not ideological purity"]]]

    [:div.approach-section
     [:h3 "Collaborative Leadership That Builds Capability"]
     [:ul.approach-list
      [:li "Breaking decision paralysis through structured facilitation when stakes are high"]
      [:li "Transferring systematic debugging approaches through deliberate pairing"]
      [:li "Creating shared understanding across stakeholders to align technical and business priorities"]]]]
   [:section.testimonial
    [:blockquote "Do you need someone who has Seen Some Shit in large, long-running, highly coupled, business critical code bases? Hire Caroline."]
    [:div.attribution "- Nat Bennett"]]

   [:section.recent-posts
    [:h2 "Recent Blog Posts"]
    [:div#recent-posts-content
     [:p "Loading recent posts..."]
     [:script "document.addEventListener('DOMContentLoaded', function() {
            fetch('/blog')
              .then(response => response.text())
              .then(html => {
                // Extract blog posts from the response
                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = html;
                const blogPosts = tempDiv.querySelector('.blog-posts');
                if(blogPosts) {
                  document.getElementById('recent-posts-content').innerHTML = blogPosts.innerHTML;
                }
              })
              .catch(error => {
                console.error('Error fetching blog posts:', error);
                document.getElementById('recent-posts-content').innerHTML = '<p>Failed to load recent posts</p>';
              });
          });"]]]])

(defn about-page []
  [:div.about-content
   [:h1 "About Us"]
   [:section.about-intro
    [:div.about-text
     [:h2 "Who We Are"]
     [:p (str "Founded in 2020, " (:company-name site-info) "provides expert software engineering services to businesses across various industries. With a focus on quality, performance, and maintainability, we help our clients build software that truly delivers value.")]
     [:p "I help engineering teams overcome the most challenging technical roadblocks by bringing deep systems expertise and structured collaboration approaches. I thrive where others get lost—in large, haunted codebases, complex debugging missions, and when teams are paralyzed by overwhelming technical challenges. I bring the battle-tested experience of someone who has 'seen some shit' in complex systems, combined with the ability to build cultural steel within your team. I'll help your engineers move from overwhelmed to empowered, transforming technical debt and process pain into sustainable engineering practices."]]
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
     [:ul.contact-details
      [:li [:i.icon.email] (:email site-info)]]]

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