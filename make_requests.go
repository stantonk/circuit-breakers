package main

import (
	"io/ioutil"
	"log"
	"net/http"
	"os"
)

func main() {
	request_counter := 0
	for true {
		resp, err := http.Get("http://localhost:8080/service/echo?input=hi")
		request_counter += 1
		print(request_counter, "\n")
		if err != nil {
			log.Fatalln(err)
			os.Exit(1)
		}
		_, err = ioutil.ReadAll(resp.Body)
		if err != nil {
			log.Fatalln(err)
			os.Exit(1)
		}
	}
}
