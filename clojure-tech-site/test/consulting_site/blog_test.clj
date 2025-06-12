(ns consulting-site.blog-test
  (:require [clojure.test :refer :all]
            [consulting-site.blog :as blog]
            [clojure.string :as str]))

(deftest test-extract-posts-from-xml
  (testing "handles nil xml gracefully"
    (is (nil? (blog/extract-posts-from-xml nil))))

  #_(testing "handles empty xml"
    (is (empty? (blog/extract-posts-from-xml {})))))
(deftest test-create-safe-excerpt
  (testing "handles nil description"
    (is (= "No excerpt available" (blog/create-safe-excerpt nil 100))))

  (testing "creates excerpt from content"
    (is (str/starts-with?
         (blog/create-safe-excerpt "This is a long description that should be truncated" 10)
         "This is a "))))