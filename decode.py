import javaobj

# Valor obtenido de Redis
valor_redis = {'��\x00\x05t\x00.77944972-b392-425a-b8bf-378877830e9c1330287311': '��\x00\x05sr\x00%com.concredito.redis.demo.entity.User\x86\x8b)1K�5\x08\x02\x00\x05Z\x00\x05adminL\x00\x05emailt\x00\x12Ljava/lang/String;L\x00\x02idq\x00~\x00\x01L\x00\x08passwordq\x00~\x00\x01L\x00\x05taskst\x00\x10Ljava/util/List;xp\x00t\x00\x1cejemploRabbitMQ7@example.comt\x00.77944972-b392-425a-b8bf-378877830e9c1330287311t\x00\x08xdlolRB7p'}

# Convertir el valor del diccionario a un objeto bytes
valor_bytes = valor_redis[list(valor_redis.keys())[0]].encode('utf-8')

# Deserializar el objeto Java
usuario_java = javaobj.loads(valor_bytes)

# Imprimir el usuario deserializado
print(usuario_java)
