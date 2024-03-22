import redis

def check_redis_connection(host, port):
    try:
        # Connect to the Redis server
        r = redis.Redis(host=host, port=port)

        # Check if the connection is alive
        return r.ping()

    except redis.ConnectionError as e:
        # Connection error occurred
        print(f"Error connecting to Redis server: {e}")
        return False

if __name__ == "__main__":
    # Change the host and port if your Redis server is running on a different address
    host = '127.0.0.1'
    port = 6379

    if check_redis_connection(host, port):
       print("Redis server is reachable.")
    else: 
        print("Redis server is not reachable.")
        
    if check_redis_connection("localhost", port):
        print("Redis server is reachable.")
    else: 
        print("Redis server is not reachable.")
