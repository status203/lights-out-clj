(ns lights-out.state.core
  (:require [cljs.spec.alpha :as s]
            [lights-out.validation :as validation]
            [re-frame.core :as rf]))

;; # Spec

;; Common
(s/def ::grid-size pos-int?)

;; Setup
(s/def ::errors (s/nilable (s/coll-of string? :kind vector?)))

(s/def ::setup (s/keys :req [::grid-size ::errors]))

;; Game
; Whether cells are lit, left to right, top-to-bottom
(s/def ::board (s/coll-of boolean?))

(s/def ::game (s/and (s/keys :req (::grid-size ::board))
                     #(= (* (::grid-size %) (::grid-size %))
                         (count (::board %)))))

(s/def ::app-schema (s/keys :req [::setup ::game]))
;; # State

(def initial-state
  {::setup {::grid-size 5
            ::errors nil}
   ::game {::grid-size 5
           ::board (into [] (repeat 25 false))}})

;; # Standard interceptors. See https://day8.github.io/re-frame/Debugging/
(defn valid-state?
  "validate the given db, writing any problems to console.error"
  [db]
  (let [res (validation/check ::app db)]
    (when (some? res)
      (.error js/console (str "schema problem: " res)))))

(def standard-interceptors
  [(when ^boolean goog.DEBUG rf/debug)
   (when ^boolean goog.DEBUG (rf/after valid-state?))])