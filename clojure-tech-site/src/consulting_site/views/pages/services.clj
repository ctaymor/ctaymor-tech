(ns consulting-site.views.pages.services)

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

(def page services-page)