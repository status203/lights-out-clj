(ns lights-out.state.game
  (:require [re-frame.core :as rf]
            
            [lights-out.domain :as domain]))

;; app-db
;;   :game
;;     :board

;; handlers

(rf/reg-event-db
 :game/new-board
 (fn [db [_ size]]
   (assoc-in db [:game :board] (domain/new-board size))))


;; subscriptions

(rf/reg-sub
 :game/board
 (fn [db [_]]
   (-> db :game :board)))
