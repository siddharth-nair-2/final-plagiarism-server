#include <iostream>

using namespace std;
int main() {
    cout << fib(5) << endl;
}

int fib(int x) {
    if (x == 0)
        return 0;

    if (x == 1)
        return 1;

    return fib(x-1)+fib(x-2);
}