(ns lights-out.state.game
  (:require [re-frame.core :as rf]
            
            [lights-out.domain :as domain]))

(def initial-state
  {:game
   {:grid-size 5
    :board (into [] (repeat 25 false))}}) ; whether cells are lit, left->right, top->bottom

(rf/reg-event-db
 :game/new-board
 (fn [db [_ size]]
   (-> db 
       (assoc-in [:game :board] (domain/new-board size))
       (assoc-in [:game :grid-size] size))))

(rf/reg-event-db
 :game/toggle-cell
 (fn [db [_ index]]
   (domain/toggle-cell (-> db :game :board) index)))

;; subscriptions

(rf/reg-sub
 :game/board
 (fn [db [_]]
   (-> db :game :board)))

(rf/reg-sub
 :game/grid-size
 (fn [db [_]]
   (-> db :game :grid-size)))