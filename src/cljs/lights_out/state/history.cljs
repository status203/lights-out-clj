(ns lights-out.state.history
  (:require [re-frame.core :as rf]
            
            [lights-out.domain :as domain]
            [lights-out.state.core :as los]))

;; Handlers


;; Subscriptions

(rf/reg-sub
 :history/count
 (fn [{history ::los/history} [_]]
   (count history)))

(rf/reg-sub
 :history/entry
 (fn [db [_ idx]]
   (let [entry (-> db ::los/history (nth idx))
         move (::los/move entry)
         size (::los/grid-size entry)
         label (domain/cell->label move size)]
     {:entry entry
      :move-label label})))