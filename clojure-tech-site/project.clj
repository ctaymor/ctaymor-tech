; project.clj - Your project dependencies
(defproject consulting-site "0.1.0-SNAPSHOT"
  :description "Professional software engineering consulting website"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring "1.9.6"]
                 [ring/ring-defaults "0.3.4"]
                 [compojure "1.7.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.json "2.4.0"]
                 [org.clojure/data.zip "0.1.3"]
                 [clj-http "3.12.3"]
                 [clj-rss "0.2.7"]
                 [markdown-clj "1.11.4"]]
  :main consulting-site.core
  :profiles {:dev {:dependencies [[ring/ring-devel "1.9.6"]]}}
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler consulting-site.core/app})