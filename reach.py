import tkinter as tk
from tkinter import messagebox
import redis
import requests
import random
import string


class RedisApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Aplicación de Redis")
        self.root.geometry("300x200")

        # Etiqueta para mostrar el estado de la conexión
        self.connection_label = tk.Label(root, text="")
        self.connection_label.pack(pady=10)

        # Botón para comprobar la conexión
        self.check_connection_btn = tk.Button(root, text="Comprobar Conexión", command=self.check_connection)
        self.check_connection_btn.pack(pady=5)

        # Botón para borrar la base de datos
        self.clear_database_btn = tk.Button(root, text="Borrar Base de Datos", command=self.clear_database)
        self.clear_database_btn.pack(pady=5)

        # Botón para recuperar todos los datos
        self.retrieve_data_btn = tk.Button(root, text="Recuperar Datos", command=self.retrieve_data)
        self.retrieve_data_btn.pack(pady=5)
        
        
        # Botón para generar datos de usuario aleatorios
        self.generate_user_btn = tk.Button(root, text="Generar Usuario Aleatorio", command=self.generate_random_user)
        self.generate_user_btn.pack(pady=5)

    def check_connection(self):
        try:
            # Intentar conectar a Redis
            r = redis.Redis(host='localhost', port=6379, db=0)
            r.ping()  # Esto lanzará una excepción si no se puede conectar
            messagebox.showinfo("Conexión a Redis", "¡Conexión exitosa a Redis!")
        except redis.ConnectionError:
            messagebox.showerror("Conexión a Redis", "Error al conectar a Redis")

    def clear_database(self):
        try:
            # Conectar a Redis y borrar la base de datos
            r = redis.Redis(host='localhost', port=6379, db=0)
            r.flushdb()
            messagebox.showinfo("Borrar Base de Datos", "¡La base de datos de Redis ha sido borrada!")
        except redis.ConnectionError:
            messagebox.showerror("Borrar Base de Datos", "Error al conectar a Redis")

    def retrieve_data(self):
        try:
            # Conectar a Redis y recuperar todas las claves y sus valores
            r = redis.Redis(host='localhost', port=6379, db=0)
            keys = r.keys()
            data = {}
            for key in keys:
                data[key.decode('utf-8')] = r.get(key).decode('utf-8')
            
            # Mostrar los datos en la ventana de diálogo
            if data:
                message = "Datos de la base de datos de Redis:\n"
                for key, value in data.items():
                    message += f"{key}: {value}\n"
                messagebox.showinfo("Datos de la Base de Datos", message)
            else:
                messagebox.showinfo("Datos de la Base de Datos", "No hay datos en la base de datos de Redis.")
        except redis.ConnectionError:
            messagebox.showerror("Recuperar Datos", "Error al conectar a Redis")
            
    def generate_random_user(self):
        email = self.generate_random_email()
        password = self.generate_random_password()
        admin = str(random.choice([True, False])).lower()

        datos = {
            "email": email,
            "password": password,
            "admin": admin
        }

        try:
            response = requests.post("http://localhost:8080/api/user", json=datos)
            if response.status_code == 200:
                messagebox.showinfo("Usuario Creado", "Se ha creado un usuario aleatorio con éxito.")
            else:
                messagebox.showerror("Error", f"Error al crear usuario: {response.text}")
        except Exception as e:
            messagebox.showerror("Error", f"Ocurrió un error al realizar la solicitud: {str(e)}")

    def generate_random_email(self):
        random_string = ''.join(random.choices(string.ascii_lowercase, k=8))
        return f"{random_string}@example.com"

    def generate_random_password(self):
        return ''.join(random.choices(string.ascii_letters + string.digits, k=8))


if __name__ == "__main__":
    root = tk.Tk()
    app = RedisApp(root)
    root.mainloop()
