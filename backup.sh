sudo docker exec -t magazinestore pg_dumpall -c -U postgres > ./dump_`date +%d-%m-%y_%H:%M:%S`.sql
