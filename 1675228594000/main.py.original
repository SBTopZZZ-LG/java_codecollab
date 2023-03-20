import sqlite3
from contextlib import closing

FILE = "database.sqlite"


# Display a row (tuple of fields) to the console
def displayRow(row):
    id, name, quantity, price = row
    print(
        f"----------\nId\t\t\t: {id}\nName\t\t: {name}\nQuantity\t: {quantity}\nPrice\t\t: {price}\n----------"
    )


# Create a connection to the database and create table (if does not exist)
def dbConnect():
    try:
        conn = sqlite3.connect(FILE)
        with closing(conn.cursor()) as c:
            try:
                c.execute('''create table products (
        id int,
        name varchar(100) not null,
        quantity int not null check (quantity > 0),
        price real not null check (price > 0),
        primary key (id)
      );''')
            except:
                pass

        conn.commit()
        return conn
    except:
        print("Failed to connect to the database")


# Insert product into database
def dbInsert(c, id, name, quantity, price):
    query = "insert into products values (?, ?, ?, ?);"
    try:
        c.execute(query, (id, name, quantity, price))
        return True
    except:
        print("Failed to insert into the database")
        return False


# Display all products from database
def dbDisplay(c):
    query = "select * from products;"
    try:
        isEmpty = True
        for row in c.execute(query).fetchall():
            displayRow(row)
            isEmpty = False
        if isEmpty:
            print("No data")
        return True
    except:
        print("Failed to display the contents of the database")
        return False


# Delete one product from database by id
def dbDeleteOne(c, id):
    query = f"delete from products where id={id};"
    try:
        c.execute(query)
        return True
    except:
        print("Failed to delete from the database")
        return False


# Boost prices by 10% for products with prices LESS THAN 50 in database
def dbBoostPriceBy10Percent(c):
    query = "update products set price = price + (price * 0.1) where price < 50;"
    try:
        c.execute(query)
        return True
    except:
        print("Failed to update in database")
        return False


# Display products with quantity LESS THAN 40 in database
def dbDisplayWhereQtyLessThan40(c):
    query = "select * from products where quantity < 40;"
    isEmpty = True
    for row in c.execute(query).fetchall():
        isEmpty = False
        displayRow(row)
    if isEmpty:
        print("No data")


def main():
    conn = dbConnect()
    if conn == None:
        print("Exitting")
        return

    choice = -1
    while choice != 9:
        try:
            choice = int(
                input(
                    "1] Insert product\n2] Display all products\n3] Delete a product by id\n4] Boost product prices by 10% where price is LESS THAN 50\n5] Display products where quantity is LESS THAN 40\n9] Exit\n> "
                ))
        except:
            continue

        print("--------------------")
        with closing(conn.cursor()) as c:
            if choice == 1 and dbInsert(c, int(input("Enter product id: ")),
                                        input("Enter product name: "),
                                        int(input("Enter product quantity: ")),
                                        float(input("Enter product price: "))):
                print("Ok.")
            elif choice == 2:
                dbDisplay(c)
            elif choice == 3 and dbDeleteOne(
                    c, int(input("Enter product id to delete: "))):
                print("Ok.")
            elif choice == 4 and dbBoostPriceBy10Percent(c):
                print("Ok.")
            elif choice == 5 and dbDisplayWhereQtyLessThan40(c):
                print("Ok.")
            elif choice == 9:
                break
        print("--------------------")
        conn.commit()


if __name__ == "__main__":
    main()
