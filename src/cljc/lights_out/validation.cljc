(ns lights-out.validation
  (:require
   #?(:clj [clojure.spec.alpha :as s]
      :cljs [cljs.spec.alpha :as s])))

(defn check
  "If spec conforms then returns nil, otherwise returns an error string"
  [spec value]
  (if (= ::s/invalid (s/conform spec value))
    (s/explain-str spec value)
    value))
