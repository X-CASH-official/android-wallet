/**
 * Copyright (c) 2017-2018, The Monero Project
 *
 * All rights reserved.
 */

#if defined(HAVE_MONERUJO)

#ifdef __cplusplus
extern "C"
{
#endif

/**
 * @brief LedgerFind - find Ledger Device and return it's name
 * @param buffer     - buffer for name of found device
 * @param len        - length of buffer
 * @return  0 - success
 *         -1 - no device connected / found
 *         -2 - JVM not found
 */
int LedgerFind(char *buffer, size_t len);

/**
 * @brief LedgerExchange - exchange data with Ledger Device
 * @param command        - buffer for data to send
 * @param cmd_len        - length of send to send
 * @param response       - buffer for received data
 * @param max_resp_len   - size of receive buffer
 *
 * @return length of received data in response or -1 if error
 */
int  LedgerExchange(unsigned char *command, unsigned int cmd_len, unsigned char *response, unsigned int max_resp_len);

#ifdef __cplusplus
}
#endif

#include "device_io.hpp"

#pragma once

namespace hw {
  namespace io {
    class device_io_monerujo: device_io {
    public:
      device_io_monerujo() {};
      ~device_io_monerujo() {};

      void init() {};
      void release() {};

      void connect(void *params) {};
      void disconnect() {};
      bool connected() const {return true;}; // monerujo is always connected before it gets here

      // returns number of bytes read or -1 on error
      int  exchange(unsigned char *command, unsigned int cmd_len, unsigned char *response, unsigned int max_resp_len) {
        return LedgerExchange(command, cmd_len, response, max_resp_len);
      }
    };
  };
};

#endif //#if defined(HAVE_MONERUJO)
