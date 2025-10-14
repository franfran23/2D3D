#!/bin/bash

# performs junit tests

echo Lines
java -cp "classes:lib/*" org.junit.runner.JUnitCore test.LinesTest
echo Player
java -cp "classes:lib/*" org.junit.runner.JUnitCore test.PlayerTest
echo Point
java -cp "classes:lib/*" org.junit.runner.JUnitCore test.PointTest
echo MyRectangle
java -cp "classes:lib/*" org.junit.runner.JUnitCore test.MyRectangleTest
