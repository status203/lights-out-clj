(ns lights-out.state.core
  (:require [cljs.spec.alpha :as s]
            [lights-out.validation :as validation]
            [re-frame.core :as rf]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; # Spec

;; Common
(s/def ::grid-size pos-int?)

;; Setup
(s/def ::errors (s/nilable (s/coll-of string? :kind vector?)))

(s/def ::setup (s/keys :req [::grid-size ::errors]))

;; Game
; Whether cells are lit, left to right, top-to-bottom
(s/def ::grid (s/coll-of boolean?))
(s/def ::hovered-cell (s/nilable string?)) ; nil when not hovering over a cell
(s/def ::board (s/and (s/keys :req [::grid-size ::grid ::hovered-cell])
                      #(<= 1 (::grid-size %) 26)
                      #(= (* (::grid-size %) (::grid-size %))
                          (count (::grid %)))))

(s/def ::move (s/nilable nat-int?))
(s/def ::post-move-grid ::grid)
(s/def ::history-entry (s/keys :req [::move ::post-move-grid]))
(s/def ::history (s/coll-of ::history-entry :kind vector?))

(s/def ::game (s/nilable
               (s/keys :req [::board ::history])))

(s/def ::app-schema (s/keys :req [::setup ::game]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; # State
(def initial-state
  {::setup {::grid-size 5
            ::errors nil}
   ::game nil})


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; # Standard interceptors. See https://day8.github.io/re-frame/Debugging/
(defn valid-state?
  "validate the given db, writing any problems to console.error"
  [db]
  (let [res (validation/check ::app-schema db)]
    (when (some? res)
      (.error js/console (str "schema problem: " res)))))

(def debug? ^boolean goog.DEBUG)
(def standard-interceptors-db
  [(when debug? rf/debug)
   (when debug? (rf/after valid-state?))])
(def standard-interceptors-fx
  [(when debug? rf/debug)    ;; as before
   (when debug? (rf/after #(when % (valid-state? %))))])

(defn reg-event-db  ;; alternative to rf/reg-event-db
  ([id handler-fn]
   (rf/reg-event-db id standard-interceptors-db handler-fn))
  ([id interceptors handler-fn]
   (rf/reg-event-db
    id
    [standard-interceptors-db interceptors]
    handler-fn)))

(defn reg-event-fx ;; alternative to reg-event-db
  ([id handler-fn]
   (rf/reg-event-fx id standard-interceptors-fx handler-fn))
  ([id interceptors handler-fn]
   (re-frame.core/reg-event-fx
    id
    [standard-interceptors-fx interceptors]
    handler-fn)))