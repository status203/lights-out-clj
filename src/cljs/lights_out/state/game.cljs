(ns lights-out.state.game
  (:require
   [lights-out.domain :as domain]
   [lights-out.state.core :as los]
   [re-frame.core :as rf]))

(los/reg-event-db
 :game/new-game
 (fn [db [_ size]]
   (-> db
       (assoc ::los/game (domain/new-game size)))))

(los/reg-event-db
 :game/toggle-cell
 (fn [db [_ cell]]
   (update-in db [::los/game ::los/board] domain/toggle-cell-in-board cell)))

;; subscriptions

(rf/reg-sub
 :game/game
 (fn [db [_]]
   (-> db ::los/game)))

(rf/reg-sub
 :game/board
 (fn [db _]
   (-> db ::los/game ::los/board)))

(rf/reg-sub
 :game/succeeded?
 :<- [:game/board]
 (fn [board _] (when board (domain/succeeded? board))))

(rf/reg-sub
 :game/grid
 :<- [:game/board]
 (fn [board [_]]
   (when board (::los/grid board))))

(rf/reg-sub
 :game/grid-size
 :<- [:game/board]
 (fn [board [_]]
   (when board (::los/grid-size board))))
