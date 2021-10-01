(ns lights-out.state.setup
  (:require 
   [lights-out.state.core :as los]
   [re-frame.core :as rf]))

;; handlers

(rf/reg-event-db
 :setup/size-changed
 (fn [db [_ new-size]]
   (if (< 0 new-size 27)
     (assoc-in db [::los/setup] {::los/grid-size new-size :errors nil})
     (assoc-in db [::los/setup ::los/errors] ["Size must be between 1 & 26"]))))

(rf/reg-event-fx
 :setup/new-game
 (fn [{db :db} _]
   {:dispatch [:game/new-board (-> db ::los/setup ::los/grid-size)]}))

;; subscriptions

(rf/reg-sub
 :setup/grid-size
 (fn [db _]
   (-> db ::los/setup ::los/grid-size)))

(rf/reg-sub
 :setup/errors
 (fn [db _]
   (-> db ::los/setup ::los/errors)))
