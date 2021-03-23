(ns lights-out.state.setup
  (:require [re-frame.core :as rf]))

;; app-db
;;   :setup
;;     :grid-size

;; handlers

(rf/reg-event-fx
 :setup/size-changed
 (fn [{db :db} [_ new-size]]
  {:db (assoc-in db [:setup :grid-size] new-size)
   :dispatch [:game/new-board new-size]}))

(rf/reg-event-fx
 :setup/init
 (fn [{db :db} [_]]
   (if (get-in db [:setup :grid-size])
     {}
     {:dispatch [:setup/size-changed 5]})))

;; subscriptions

(rf/reg-sub
 :setup/grid-size
 (fn [db _]
   (-> db :setup :grid-size)))

