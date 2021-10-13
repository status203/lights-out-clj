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
   (update-in db [::los/game] domain/toggle-cell-in-game cell)))

(los/reg-event-db
 :game/enter-cell
 (fn [db [_ cell-label]]
   (assoc-in db [::los/game ::los/board ::los/hovered-cell] cell-label)))

(los/reg-event-db
 :game/leave-cell
 (fn [db [_]]
   (assoc-in db [::los/game ::los/board ::los/hovered-cell] nil)))

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
 :game/hovered-cell
 :<- [:game/board]
 (fn [board [_]]
   (when board (::los/hovered-cell board))))

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

(rf/reg-sub
 :game/history
 :<- [:game/game]
 (fn [game [_]]
   (when game (::los/history game))))

(rf/reg-sub
 :game/completed?
 :<- [:game/grid]
 (fn [grid _] (when grid (domain/completed? grid))))
