(ns lights-out.domain)

(defn new-board
  "Returns a vector of true/false indicating which cells are lit. Cells run
  left->right, top->bottom"
  [size]
  (into [] (repeatedly (* size size) #(pos? (rand-int 2)))))

(defn neighbours
  "Returns a vector of indexs for a cell's neighbours"
  [size cell]
  (let [top?    (= 0 (quot cell size))
        right?  (= (dec size) (rem cell size))
        bottom? (= (dec size) (quot cell size))
        left?   (= 0 (rem cell size))]
    (filter identity [(when-not top? (- cell size))
                      (when-not left? (dec cell))
                      (when-not right? (inc cell))
                      (when-not bottom? (+ cell size))])))

(defn toggle-cell
  "Given a board and the index of a cell, returns a new board with the specified
   cell (and it's cardinal neighbours lit status toggled)"
  [board size cell]
  (println {:board board :size size :cell cell})
  (reduce (fn [board cell] (update-in board [cell] not))
          board
          (conj (neighbours size cell)
                cell)))

(comment
  (def board [true  true  true
              false false false
              true true true])
  (neighbours 3 0)
  (filter identity (neighbours 3 0))
  board
  (update-in board [0] not)
  (update-in [true true false] [0] not)
  (toggle-cell board 3 4)
  (quot 0 3)
  )