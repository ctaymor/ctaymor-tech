(ns consulting-site.rss-sync-test
  (:require [clojure.test :refer :all]
            [consulting-site.rss-sync :as rss]
            [clojure.string :as str]))

(deftest test-extract-posts-from-xml
  (testing "handles nil xml gracefully"
    (is (nil? (rss/extract-posts-from-xml nil))))

  #_(testing "handles empty xml"
    (is (empty? (rss/extract-posts-from-xml {}))))
(deftest test-create-safe-excerpt
  (testing "handles nil description"
    (is (= "No excerpt available" (rss/create-safe-excerpt nil 100))))

  (testing "creates excerpt from content"
    (is (str/starts-with?
         (rss/create-safe-excerpt "This is a long description that should be truncated" 10)
         "This is a "))))