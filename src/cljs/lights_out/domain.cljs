(ns lights-out.domain
  (:require
   [lights-out.state.core :as los]))

(defn succeeded?
  "Returns whether all cells in a supplied grid are off"
  [grid]
  (every? not grid))

(defn- new-grid
  "Returns a vector of true/false indicating which cells are lit. Cells run
  left->right, top->bottom"
  [size]
  (into [] (repeatedly (* size size) #(pos? (rand-int 2)))))

(defn new-game
  "Returns a new (unfinished) game."
  [size]
  {::los/board 
   {::los/grid-size size
    ::los/grid (first (filter (complement succeeded?)
                              (repeatedly #(new-grid size))))
    ::los/hovered-cell "A1"}
   ::los/history []})

(defn- neighbours
  "Returns a vector of indexes for a cell's neighbours"
  [size cell]
  (let [top?    (= 0 (quot cell size))
        right?  (= (dec size) (rem cell size))
        bottom? (= (dec size) (quot cell size))
        left?   (= 0 (rem cell size))]
    (filter identity [(when-not top? (- cell size))
                      (when-not left? (dec cell))
                      (when-not right? (inc cell))
                      (when-not bottom? (+ cell size))])))

(defn- toggle-cell-in-grid
  "Given a grid, grid-size and cell, returns a new grid with the specified cell
   (and it's cardinal neighbours) lit status toggled"
  [grid grid-size cell]
  (reduce (fn [grid cell] (update-in grid [cell] not))
          grid (conj (neighbours grid-size cell)
                     cell)))

(def col-labels (into [] (for [col (range 26)] (char (+ 65 col)))))
(def row-labels (into [] (for [row (range 26)] (str (inc row)))))


(defn cell->label
  [cell size]
  (str (get col-labels (rem cell size))
       (get row-labels (quot cell size))))

(defn update-hover-value
  "Makes the hovered-cell nil if the game is complete"
  [game]
  (if (succeeded? (get-in game [::los/board ::los/grid]))
    (assoc-in game [::los/board ::los/hovered-cell] nil)
    game))

(defn toggle-cell-in-game
  "Given an unfinished board and the index of a cell, returns a new board with 
   the specified cell and it's cardinal neighbours lit status toggled"
  [{{:keys [::los/grid ::los/grid-size ::los/hovered-cell]} ::los/board :as game}
   cell]
  (-> game
      (assoc-in [::los/board ::los/grid] (toggle-cell-in-grid grid grid-size cell))
      (update-in [::los/history] conj cell)
      update-hover-value))


