(ns lights-out.domain
  (:require
   [lights-out.state.core :as los]))

(defn new-game
  "Returns a vector of true/false indicating which cells are lit. Cells run
  left->right, top->bottom"
  [size]
  {::los/board {::los/grid-size size
           ::los/grid (into [] (repeatedly (* size size) #(pos? (rand-int 2))))}})

(defn succeeded?
  "Returns whether all cells in a supplied board are off"
  [board]
  (every? not (::los/grid board)))

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
   (and it's cardinal neighbours lit status toggled)"
  [grid grid-size cell]
  (reduce (fn [grid cell] (update-in grid [cell] not))
          grid (conj (neighbours grid-size cell)
                     cell)))

(defn toggle-cell-in-board
  "Given a board and the index of a cell, returns a new board with the specified
   cell (and it's cardinal neighbours lit status toggled)"
  [{:keys [::los/grid ::los/grid-size] :as board} cell]
  (assoc-in board 
            [::los/grid]
            (toggle-cell-in-grid grid grid-size cell)))
