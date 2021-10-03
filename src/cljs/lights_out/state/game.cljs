(ns lights-out.state.game
  (:require
   [lights-out.domain :as domain]  
   [lights-out.state.core :as los]
   [re-frame.core :as rf]))

(los/reg-event-db
 :game/new-board
 (fn [db [_ size]]
   (-> db
       (assoc-in [::los/game ::los/board] (domain/new-board size)))))

(los/reg-event-db
 :game/toggle-cell
 (fn [db [_ cell]]
   (update-in db [::los/game ::los/board] domain/toggle-cell-in-board cell)))

;; subscriptions

(rf/reg-sub
 :game/grid
 (fn [db [_]]
   (-> db ::los/game ::los/board ::los/grid)))

(rf/reg-sub
 :game/grid-size
 (fn [db [_]]
   (-> db ::los/game ::los/board ::los/grid-size)))