(ns lights-out.validation
  (:require
   #?(:clj [clojure.spec.alpha :as s]
      :cljs [cljs.spec.alpha :as s])))

(defn check
  "If spec conforms then returns nil, otherwise returns an error string"
  [spec value]
  (if (= ::s/invalid (s/conform spec value))
    (s/explain-str spec value)
    nil))

(defn validate
  "If spec conforms then returns nil, otherwise returns an error string"
  [spec value]
  (if (= ::s/invalid (s/conform spec value))
    (let [error (s/explain-str spec value)]
      #?(:clj (throw (AssertionError. error)))
      #?(:cljs (js/Error error)))
    nil))
