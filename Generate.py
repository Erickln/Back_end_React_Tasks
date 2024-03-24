import redis

# Conexión a Redis
redis_host = 'localhost'
redis_port = 6379
redis_db = 0
redis_client = redis.StrictRedis(host=redis_host, port=redis_port, db=redis_db)

# Datos del usuario a agregar
user_data = {
    "email": "ejemploRabbitMQ7@example.com",
    "password": "xdlolRB7",
    "admin": "false"
}

# Clave para almacenar los datos del usuario en Redis
user_key = "user:{}".format(user_data["email"])

# Agregar los datos del usuario a Redis
redis_client.hmset(user_key, user_data)

print("Usuario agregado a Redis con éxito.")
