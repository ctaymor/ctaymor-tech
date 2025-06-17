(ns consulting-site.rss-sync-test
  (:require [clojure.test :refer :all]
            [consulting-site.rss-sync :as rss]
            [clojure.string :as str]))

(deftest test-extract-posts-from-xml
  (testing "handles nil xml gracefully"
    (is (nil? (rss/extract-posts-from-xml nil))))

;;  #_(testing "handles empty xml"(is (empty? (rss/extract-posts-from-xml {})))
  )

(deftest test-create-safe-excerpt
  (testing "handles nil description"
    (is (= "No excerpt available" (rss/create-safe-excerpt nil 100))))

  (testing "creates excerpt from content"
    (is (str/starts-with?
         (rss/create-safe-excerpt "This is a long description that should be truncated" 10)
         "This is a "))))

(deftest test-strip-html
  (testing "removes script tag hidden in html entities - prevent script injection"
    (is (not (str/includes?
             (rss/strip-html
              "&lt;script&gt;alert('bad')&lt;/script&gt;")
             "<script>")))))

(deftest test-sanitize-html
  (testing "removes script tag hidden in html entities - prevent script injection"
    (is (not (str/includes?
             (rss/sanitize-html
              "&lt;script&gt;alert('bad')&lt;/script&gt;")
             "<script>"))))
  (testing "Removes non-allowed tags"
        (is (not (str/includes?
                 (rss/sanitize-html
                  "<iframe>;alert('bad')</iframe>")
                 "<iframe>"))))
    (testing "Removes non-allowed tags"
          (is (str/includes?
                   (rss/sanitize-html
                    "<a href=\"google.com\">Good Bolded Text</a>")
                   "<a href=\"google.com\">")))

    )