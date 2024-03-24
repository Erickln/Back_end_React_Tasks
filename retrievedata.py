import redis
import pickle

# Conexión a Redis
redis_host = 'localhost'
redis_port = 6379
redis_db = 0
redis_client = redis.StrictRedis(host=redis_host, port=redis_port, db=redis_db)

# Obtener todas las claves almacenadas en Redis
keys = redis_client.keys()

# Recorrer las claves y recuperar los valores asociados
# Recorrer las claves y recuperar los valores asociados
data = {}
for key in keys:
    # Obtener el valor asociado a la clave
    value = redis_client.hgetall(key)
    # Decodificar los bytes a string si es necesario
    decoded_value = {}
    for k, v in value.items():
        try:
            decoded_key = k.decode('utf-8')
            decoded_val = v.decode('utf-8')
        except UnicodeDecodeError:
            decoded_key = k.decode('latin-1')  # Intenta otra codificación si falla UTF-8
            decoded_val = v.decode('latin-1')
        decoded_value[decoded_key] = decoded_val
    # Almacenar los datos en un diccionario
    data[key.decode('utf-8')] = decoded_value
    # Recorriendo los datos recuperados de Redis
    for key, value in data.items():
        print("Clave:", key)
        # Imprimir el valor del diccionario
        print("Valor:", value)
        decoded_user = pickle.loads(value)
        print(decoded_user)


# Imprimir los datos recuperados
print("Datos de Redis:")
# print(data)
