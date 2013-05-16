PID=$(<pid.txt)
kill -9 $PID
rm pid.txt