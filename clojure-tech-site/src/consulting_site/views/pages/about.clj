(ns consulting-site.views.pages.about
  (:require
;;   [clojure.string :as str]
;;   [hickory.core :as hickory]
;;   [hickory.select :as s]
   [consulting-site.config :refer [site-info]]))

(defn about-page []
  [:div.about-content
   [:h1 "About Me"]
   [:section.about-intro
    [:div.about-text
     [:h2 "Who I am"]
     [:p (str "Founded in 2020, " (:company-name site-info) " provides expert software engineering services to businesses across various industries. With a focus on quality, performance, and maintainability, we help our clients build software that truly delivers value.")]

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

(def page about-page)

