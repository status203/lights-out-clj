(ns lights-out.state.core
  (:require [cljs.spec.alpha :as s]
            [lights-out.validation :as validation]
            [re-frame.core :as rf]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; # Spec

;; Common
(s/def ::grid-size pos-int?)
; Whether cells are lit, left to right, top-to-bottom
(s/def ::grid (s/coll-of boolean?))
(s/def ::move (s/nilable nat-int?))
(s/def ::board (s/and (s/keys :req [::grid-size ::grid ::move])
                      #(<= 1 (::grid-size %) 26)
                      #(= (* (::grid-size %) (::grid-size %))
                          (count (::grid %)))
                      #(< (::move %) (* (::grid-size %) (::grid-size %)))))

;; Setup
(s/def ::errors (s/nilable (s/coll-of string? :kind vector?)))

(s/def ::setup (s/keys :req [::grid-size ::errors]))


;; Game
(s/def ::game (s/nilable ::board))


;; History
(s/def ::post-move-board ::board)
(s/def ::history (s/nilable (s/coll-of ::post-move-board :kind vector?)))


;; Display
(s/def ::display-type #{:game :history})
(s/def ::display (s/nilable (s/keys :req [::display-type ::board])))
(s/def ::stored-display ::display)


(s/def ::app-schema (s/keys :req [::setup ::game ::history ::display]
                            :opt [::stored-display]))

;; Options
(s/def ::highlight-type #{:none :move-only :all-affected})
(s/def ::hypothetical-highlight ::highlight-type)
(s/def ::historical-highlight ::highlight-type)
(s/def ::options (s/keys :req [::hypothetical-highlight
                               ::historical-highlight]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; # State
(def initial-state
  {::setup {::grid-size 5
            ::errors nil}
   ::options {::hypothetical-highlight :none
              ::historical-highlight :none}
   ::game nil
   ::display nil
   ::history nil})


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; # Standard interceptors. 
;; See https://day8.github.io/re-frame/Debugging/
;;
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