(ns lights-out.state.game
  (:require
   [lights-out.domain :as domain]  
   [lights-out.state.core :as los]
   [re-frame.core :as rf]))

(rf/reg-event-db
 :game/new-board
 (fn [db [_ size]]
   (-> db 
       (assoc-in [::los/game ::los/board] (domain/new-board size))
       (assoc-in [::los/game ::los/grid-size] size))))

(rf/reg-event-db
 :game/toggle-cell
 (fn [db [_ index]]
   (domain/toggle-cell (-> db ::los/game ::los/board) index)))

;; subscriptions

(rf/reg-sub
 :game/board
 (fn [db [_]]
   (-> db ::los/game ::los/board)))

(rf/reg-sub
 :game/grid-size
 (fn [db [_]]
   (-> db ::los/game ::los/grid-size)))