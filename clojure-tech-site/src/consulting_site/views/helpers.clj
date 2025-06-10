(ns consulting-site.views.helpers
  (:require    [hickory.core :as hickory]))

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
