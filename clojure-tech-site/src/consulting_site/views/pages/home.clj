(ns consulting-site.views.pages.home
  (:require
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

;; Export the function
(def page home-page)