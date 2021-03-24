(ns lights-out.state.setup
  (:require [re-frame.core :as rf]))

(def initial-state
  {:setup
   {:grid-size 5
    :errors nil ; nil, or vector of error messages.
    }})

;; handlers

(rf/reg-event-db
 :setup/size-changed
 (fn [db [_ new-size]]
   (if (< 0 new-size 27)
     (assoc-in db [:setup] {:grid-size new-size :errors nil})
     (assoc-in db [:setup :errors] ["Size must be between 1 & 26"]))))

(rf/reg-event-fx
 :setup/new-game
 (fn [{db :db} _]
   {:dispatch [:game/new-board (-> db :setup :grid-size)]}))

;; subscriptions

(rf/reg-sub
 :setup/grid-size
 (fn [db _]
   (-> db :setup :grid-size)))

(rf/reg-sub
 :setup/errors
 (fn [db _]
   (-> db :setup :errors)))
