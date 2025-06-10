(ns consulting-site.views.pages.contact
  (:require
     [consulting-site.config :refer [site-info]]))

(defn contact-page []
  [:div.contact-content
   [:h1 "Contact Us"]
   [:div.contact-container
    [:div.contact-info
     [:h2 "Get In Touch"]
     [:p "I'd love to hear about your project. Reach out to discuss how I can help with your software engineering needs."]
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

(def page contact-page)